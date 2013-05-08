package org.appverse.web.tools.projectgenerator.plugin;

public enum PresentationLayerType {
	JSF2("JSF2"),GWT("GWT");
	
	private final String PRESENTATION_LAYER_TYPE;
		
	private PresentationLayerType(String presentationLayerType) {
	        this.PRESENTATION_LAYER_TYPE = presentationLayerType;
	}
	
	public boolean equals(String presentationLayerType){
		return  this.PRESENTATION_LAYER_TYPE.equals(presentationLayerType);
	}
	
	public String getValue(){
		return this.PRESENTATION_LAYER_TYPE;
	}
}
