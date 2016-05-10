package security.rest.persistence.common;

import javax.persistence.AttributeConverter;

/**
 * Boolean/Integer value converter. Enable declares int properties as booleans.
 *
 * @author Juan Rada
 */
public class BooleanConverter implements AttributeConverter<Boolean, Integer>
{
    @Override
    public Integer convertToDatabaseColumn(Boolean attribute)
    {
        return attribute ? 1 : 0;
    }

    @Override
    public Boolean convertToEntityAttribute(Integer dbData)
    {
        return dbData == 1;
    }
}
