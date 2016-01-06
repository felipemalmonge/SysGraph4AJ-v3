package model;

import java.lang.reflect.Modifier;
import java.util.HashSet;
import java.util.Set;

import analysis.SysAnalysis;

public class SysField implements SysElement {
	
	private String type;
	private String name;
	private String visibility;
	private SysClass owner;
	private boolean isStatic;


	public SysField(boolean isStatic, String type, String name, String visibility) {
		this.type=type;
		this.name=name;
		this.visibility = visibility;
		this.isStatic=isStatic;
	}

	public SysField(java.lang.reflect.Field field) {
		this(Modifier.isStatic(field.getModifiers()), 
				field.getType().getName(),
				field.getName(),
				SysAnalysis.getVisibility(field.getModifiers()));
	}

	@Override
	public boolean equals(SysElement a){
		if(this.type.equals(((SysField) a).getType()) && this.name.equals(a.getName())) return true;
		return false;
	}

	@Override
	public String getFullyQualifiedName() {
		if(owner!=null)return owner.getFullyQualifiedName()+"."+this.name;
		return name;
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public SysElement getOwner(){
		return this.owner;
	}

	public String getType() {
		return this.type;
	}

	public String getVisibility(){
		return this.visibility;
	}

	public boolean isStatic() {
		return this.isStatic;
	}

	@Override
	public void setOwner(IElement owner){
		this.owner=(SysClass)owner;
	}


	@Override
	public SysElement partialClone() {
		SysField f = new SysField(isStatic, type, name, visibility);
		f.setOwner(owner);
		return f;
	}


	@Override
	public Set<IElement> getChildElements() {
		return new HashSet<IElement>();
	}


	@Override
	public SysElement get(String thisName, String sig, boolean isLast) {
		return null;
	}

	@Override
	public SysElement getMax(String called, String sig) {
		return null;
	}

	@Override
	public void addChild(IElement e){
		//do nothing
	}

	@Override
	public String toString(){
		return this.getType() + " -> " +this.getFullyQualifiedName();
	}

	@Override
	public String viewState() {
		String state = "";
		state+="Fully qualified name: "+this.getFullyQualifiedName();
		state+="\nType: "+this.getType();
		state+="\nVisibility: "+this.getVisibility();
		state+="\nStatic: "+this.isStatic();
		return state;
	}




}
