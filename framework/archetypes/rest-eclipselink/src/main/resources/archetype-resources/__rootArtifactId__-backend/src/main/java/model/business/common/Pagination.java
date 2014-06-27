#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ${package}.model.business.common;

import ${package}.helpers.StringEnumSerializer;
import org.codehaus.jackson.map.annotate.JsonSerialize;

/**
 *
 * @author maps
 */
@JsonSerialize(using=StringEnumSerializer.class)
public enum Pagination {
    First, Previous, Next
}
