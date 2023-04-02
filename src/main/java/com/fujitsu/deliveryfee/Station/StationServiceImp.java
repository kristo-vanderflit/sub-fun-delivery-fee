package com.fujitsu.deliveryfee.Station;

import com.fujitsu.deliveryfee.City.City;
import com.fujitsu.deliveryfee.City.CityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@Service
public class StationServiceImp implements StationService {

    @Autowired
    StationRepository stationRepository;
    @Autowired
    CityRepository cityRepository;

    @Value("${station.weather.url}")
    private String weatherUrl;

    /**
     * Cronjob is requesting weather data from the weather portal of the Estonian Environment Agency.
     * Weather data will be added based on cities in the database.
     */
    @Override
    @Scheduled(cron = "${cron.expression.value}")
    public void saveStations() throws IOException, SAXException, ParserConfigurationException {

        //get XML objects
        DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document document = builder.parse(new URL(weatherUrl).openStream());
        NodeList list = document.getElementsByTagName("station");
        document.getDocumentElement().normalize();

        final String timestamp =
                String.valueOf(Long.parseLong(document.getDocumentElement().getAttribute("timestamp")));

        //Converting NodeList to stream nodes
        Stream<Node> nodeStream = IntStream.range(0, list.getLength())
                .mapToObj(list::item);

        //Converting stream nodes to element list
        List<Element> elements = nodeStream.map(element -> (Element) element).toList();

        //get all cities(wmocode) that represent in the database
        List<String> existingCities = cityRepository.findAll().stream()
                        .map(City::getStationCode)
                        .map(Object::toString)
                        .toList();

        //Adding station based on existing cities in the database
        List<Station> stationList = elements.stream()
                .filter(element ->
                        existingCities.contains(element.getElementsByTagName("wmocode").item(0).getTextContent()))
                .map(element -> {

                    String s1 = getValue("wmocode", element);
                    String s2 = getValue("airtemperature", element);
                    String s3 = getValue("windspeed", element);
                    String s4 = getValue("phenomenon", element);

                    return Station.builder()
                            .name(element.getElementsByTagName("name").item(0).getTextContent())
                            .wmocode(s1.isEmpty() ? 0 : Integer.parseInt(s1))
                            .airTemperature(s2.isEmpty() ? 0 : Double.parseDouble(s2))
                            .windSpeed(s3.isEmpty() ? 0 : Double.parseDouble(s3))
                            .phenomenon(s4)
                            .timestamp(timestamp)
                            .build();

                }).toList();

        stationRepository.saveAll(stationList);
    }

    /**
     * Finds last timestamp
     * @return timestamp
     */
    @Override
    public String getLastTimestamp() {
        return stationRepository.findAll().stream().reduce((first, second) -> second).map(Station::getTimestamp).orElse(null);
    }

    /**
     * Checks value that is in Element
     * @param value tag name of XML
     * @param element Element of weather station
     * @return given field of element
     */
    private String getValue(String value, Element element) {
        String result = element.getElementsByTagName(value).item(0).getTextContent();

        if (result == null || result.trim().isEmpty()) {
            return "";
        }

        return result;
    }
}
