<#--

Copyright (C) 2011 Markus Junginger, greenrobot (http://greenrobot.de)     
                                                                           
This file is part of greenDAO Generator.
                                                                           
greenDAO Generator is free software: you can redistribute it and/or modify 
it under the terms of the GNU General Public License as published by       
the Free Software Foundation, either version 3 of the License, or          
(at your option) any later version.                                        
greenDAO Generator is distributed in the hope that it will be useful,      
but WITHOUT ANY WARRANTY; without even the implied warranty of             
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the              
GNU General Public License for more details.                               
                                                                           
You should have received a copy of the GNU General Public License          
along with greenDAO Generator.  If not, see <http://www.gnu.org/licenses/>.

-->
package ${entity.javaPackage};

import de.greenrobot.dao.DaoEnum;
import java.util.HashMap;
import java.util.Map;

public enum ${entity.entityEnum.enumName} implements DaoEnum {
    <#list entity.entityEnum.values as value>
    ${value.name}(${value.tag})<#if value_has_next>,<#else>;</#if>
    </#list>

    private static final Map<Long, ${entity.entityEnum.enumName}> intToTypeMap = new HashMap<Long, ${entity.entityEnum.enumName}>();

    static {
        for (${entity.entityEnum.enumName} type : ${entity.entityEnum.enumName}.values()) {
            intToTypeMap.put(type.value, type);
        }
    }

    public static ${entity.entityEnum.enumName} fromInt(long i) {
        ${entity.entityEnum.enumName} type = intToTypeMap.get(Long.valueOf(i));
        return type;
    }

    private final long value;

    private ${entity.entityEnum.enumName}(long value) {
        this.value = value;
    }

    @Override
    public long getValue() {
        return value;
    }
}