package security.rest.persistence.common;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

/**
 * Test suite for {@link BooleanConverter}
 *
 * @author Juan Rada
 *
 */
public class BooleanConverterTest
{
    private BooleanConverter testInstance = new BooleanConverter();

    @Test
    public void testConvertToDatabaseColumn() throws Exception
    {
        assertThat(testInstance.convertToDatabaseColumn(true), is(1));
        assertThat(testInstance.convertToDatabaseColumn(false), is(0));
    }

    @Test
    public void testConvertToEntityAttribute() throws Exception
    {
        assertThat(testInstance.convertToEntityAttribute(1), is(true));
        assertThat(testInstance.convertToEntityAttribute(0), is(false));
    }
}