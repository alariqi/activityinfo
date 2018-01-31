package org.activityinfo.model.expr;
/*
 * #%L
 * ActivityInfo Server
 * %%
 * Copyright (C) 2009 - 2013 UNICEF
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the 
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public 
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */

import org.activityinfo.model.expr.eval.EvalContext;
import org.activityinfo.model.resource.ResourceId;
import org.activityinfo.model.type.FieldType;
import org.activityinfo.model.type.FieldValue;

import javax.annotation.Nonnull;

/**
 * @author yuriyz on 6/2/14.
 */
public class SymbolExpr extends ExprNode {

    @Nonnull
    private String name;

    public SymbolExpr(@Nonnull String name) {
        this.name = name;
    }

    public SymbolExpr(ResourceId id) {
        this(id.asString());
    }

    public SymbolExpr(Token token) {
        assert token.getType() == TokenType.SYMBOL;
        this.name = token.getString();
        this.sourceRange = new SourceRange(token);
    }

    @Override
    public FieldValue evaluate(EvalContext context) {
        return context.resolveSymbol(name);
    }

    @Override
    public FieldType resolveType(EvalContext context) {
        return context.resolveSymbolType(name);
    }

    public String getName() {
        return name;
    }

    public void setName(@Nonnull String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        SymbolExpr that = (SymbolExpr) o;
        return name.equals(that.name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    public ResourceId asResourceId() {
        return ResourceId.valueOf(name);
    }
    
    public String asExpression() {
        if(needsEscaping(name)) {
            return "[" + name + "]";
        } else {
            return name;
        }
    }
    
    public static boolean needsEscaping(String name) {
        assert name.length() != 0;
        
        if(!Character.isLetter(name.charAt(0))) {
            return true;
        }
        for(int i=0;i<name.length();++i) {
            char c = name.charAt(i);
            if(!Character.isLetterOrDigit(c) && c != '_') {
                return true;
            }
        }
        return false;
    }

    @Override
    public <T> T accept(ExprVisitor<T> visitor) {
        return visitor.visitSymbol(this);
    }

    @Override
    public String toString() {
        return asExpression();
    }
}