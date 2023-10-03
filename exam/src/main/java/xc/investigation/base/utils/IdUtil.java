package xc.investigation.base.utils;

import cn.hutool.core.lang.Snowflake;

import java.util.UUID;

/**
 * @author ibm
 */
public class IdUtil {

    private static final Snowflake SNOWFLAKE = new Snowflake(1,1);

    public static Long generateId(){
        return SNOWFLAKE.nextId();
    }

    public static String generateToken(){
        UUID uuid = UUID.randomUUID();
        return uuid.toString();
    }
}
