package co.nubetech.crux.action;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import org.junit.Test;
import java.util.ArrayList;
import java.util.List;

import co.nubetech.crux.dao.MappingDAO;
import co.nubetech.crux.model.Mapping;
import co.nubetech.crux.model.Report;
import co.nubetech.crux.util.CruxException;

public class TestPreviewAction {

	@Test
	public void findPreviewReportData() throws CruxException{
		Mapping mapping1 = new Mapping();
		mapping1.setId(121);
		Mapping mapping2 = new Mapping();
		mapping2.setId(122);
		Mapping mapping3 = new Mapping();
		mapping3.setId(123);
		
		Report report = new Report();
		
		ArrayList<Mapping> mappingList = new ArrayList<Mapping>();
		mappingList.add(mapping1);
		mappingList.add(mapping2);
		mappingList.add(mapping3);
		
		MappingDAO mockedMappingDAO = mock(MappingDAO.class);
		PreviewAction previewAction = new PreviewAction();
		previewAction.setMappingDAO(mockedMappingDAO);
		when(mockedMappingDAO.findById((long)121)).thenReturn(mapping1);
		when(mockedMappingDAO.findById((long)122)).thenReturn(mapping2);
		when(mockedMappingDAO.findById((long)123)).thenReturn(mapping3);
		
		List<ArrayList> dataList = new ArrayList<ArrayList>();
		
		// problem : how do i make return findById the id i want?
		
		
	}
}
