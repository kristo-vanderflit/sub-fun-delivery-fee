package com.fujitsu.deliveryfee.Station;

import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

public interface StationService {

    void saveStations() throws IOException, SAXException, ParserConfigurationException;
    String getLastTimestamp();
}
