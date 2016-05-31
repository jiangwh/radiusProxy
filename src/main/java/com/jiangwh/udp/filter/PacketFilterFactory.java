package com.jiangwh.udp.filter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.jiangwh.constan.Constan;

@Component("packetFilterFactory")
public class PacketFilterFactory {

	@SuppressWarnings("unused")
	private Logger logger = Logger.getLogger(PacketFilterFactory.class);

	private List<PacketFilterFacade> filters;

	@PostConstruct
	public void scanFilters() {
		String classPath = this.getClass().getResource(".").getPath();
		String scanClassPath = classPath.concat(File.separator).concat(
				Constan.FILTER_RELATIVELY_CLASS_PATH);
		if (null == filters) {
			filters = new ArrayList<PacketFilterFacade>();
		}
		filters.clear();
		File classDir = new File(scanClassPath);
		for (String classFileName : classDir.list()) {
			try {
				String className = this.getClass().getName()
						.substring(0,
								this.getClass().getName().lastIndexOf(
												this.getClass().getSimpleName()))
						.concat(Constan.FILTER_RELATIVELY_CLASS_PATH)
						.concat(Constan.STRING_DOT).concat(classFileName);
				PacketFilterFacade facade = (PacketFilterFacade) Class.forName(
						className.substring(0, className
								.lastIndexOf(Constan.CLASS_FILE_SUFFIX))).newInstance();
				filters.add(facade);
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
	}

	public List<PacketFilterFacade> getPacketFilters() {
		if (null == filters) {
			filters = new ArrayList<PacketFilterFacade>();
		}
		return filters;
	}

}
