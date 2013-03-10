package com.TeamNovus.Persistence.Internal;

import java.lang.reflect.Field;
import java.util.LinkedList;

import com.TeamNovus.Persistence.Annotations.Table;

public class SubTableRegistration extends TableRegistration {
	public enum RelationshipType {
		ONE_TO_ONE,
		ONE_TO_MANY;
	}
	
	private RelationshipType relationshipType = null;
	private ColumnRegistration foreignKey = null;
	private TableRegistration parentTableRegistration = null;
	private Field parentField = null;

	public SubTableRegistration(Table annotation, Class<?> tableClass, RelationshipType relationshipType, ColumnRegistration id, ColumnRegistration foreignKey, LinkedList<ColumnRegistration> columns, TableRegistration parentTableRegistration, Field parentField) {	
		super(annotation, tableClass, foreignKey, columns);
		this.relationshipType = relationshipType;
		this.foreignKey = foreignKey;
		this.parentTableRegistration = parentTableRegistration;
		this.parentField = parentField;
	}
	
	public RelationshipType getRelationshipType() {
		return relationshipType;
	}

	public ColumnRegistration getForeignKey() {
		return foreignKey;
	}

	public TableRegistration getParentTableRegistration() {
		return parentTableRegistration;
	}
	
	public Field getParentField() {
		return parentField;
	}

}
