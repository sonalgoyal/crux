/**
 * Copyright 2011 Nube Technologies
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed
 * under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR
 * CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and limitations under the License.
 */
package co.nubetech.crux.dao;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.log4j.Logger;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class DBConnection {

	private final static Logger logger = Logger
			.getLogger(co.nubetech.crux.dao.DBConnection.class);

	private static java.sql.Connection conn;
	private static Statement stmt;

	@BeforeClass
	public static void createConnection() {
		InputStream hibernateCfg = null;
		try {
			try {
				hibernateCfg = DBConnection.class
						.getResourceAsStream("/hibernate.cfg.xml");
			} catch (Exception e) {
				hibernateCfg = DBConnection.class
						.getResourceAsStream("hibernate.cfg.xml");
			}

			// get the factory
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			// Using factory get an instance of document builder
			DocumentBuilder db = dbf.newDocumentBuilder();

			// parse using builder to get DOM representation of the XML file
			Document dom = db.parse(hibernateCfg);

			// get the root element
			Element docEle = (Element) ((Document) dom).getDocumentElement();

			// get a nodelist of elements
			NodeList nodeList = docEle.getElementsByTagName("property");

			logger.debug("Printing first four values inside the 'property' element : ");
			logger.debug(" name ="
					+ nodeList.item(0).getAttributes().item(0).getNodeValue()
					+ "  value =" + nodeList.item(0).getTextContent());
			logger.debug(" name ="
					+ nodeList.item(1).getAttributes().item(0).getNodeValue()
					+ "  value =" + nodeList.item(1).getTextContent());
			logger.debug(" name ="
					+ nodeList.item(2).getAttributes().item(0).getNodeValue()
					+ "  value =" + nodeList.item(2).getTextContent());
			logger.debug(" name ="
					+ nodeList.item(3).getAttributes().item(0).getNodeValue()
					+ "  value =" + nodeList.item(3).getTextContent());

			String driverClass = nodeList.item(0).getTextContent();
			String url = nodeList.item(1).getTextContent();
			String userName = nodeList.item(2).getTextContent();
			String password = nodeList.item(3).getTextContent();
			Class.forName(driverClass);
			conn = DriverManager.getConnection(url, userName, password);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// close the input stream
			try {
				hibernateCfg.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public static Statement getStatement() {
		try {
			stmt = conn.createStatement();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return stmt;
	}

	public static void closeStatement() throws SQLException {
		stmt.close();
	}
}
