package ${package}.gwtfrontend.main;

import ${package}.gwtfrontend.common.ApplicationImages;
import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ImageResource;

public interface MainImages extends ApplicationImages {

	MainImages INSTANCE = GWT.create(MainImages.class);

}
