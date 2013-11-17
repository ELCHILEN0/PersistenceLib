package com.TeamNovus.Persistence.Queries.Expression;

import com.TeamNovus.Persistence.Databases.Enums.Junction;

public abstract class Condition {
	// Reference to the expression
	private Expression expression;
	
	// Check if the condition is negated.
	private boolean negated = false;
	
	// The junction that follows the condition.
	private Junction junction;

	public Condition(Expression expression) {
		this.expression = expression;
		
		this.negated = expression.negating;
		expression.negating = false;
		
		expression.conditions.add(this);
	}
	
	// Junctions
	public Expression and() {
		junction = Junction.AND;
		
		return expression;
	}
	
	public Expression or() {
		junction = Junction.OR;
		
		return expression;		
	}
	
	// Getters
	public boolean isNegated() {	
		return negated;
	}
	
	public Junction getJunction() {
		return junction;
	}
	
	public Expression getExpression() {
		return expression;
	}
}
