package security.rest.persistence.common;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Calendar;
import org.junit.Test;

/**
 * Test suite for {@link LocalDateTimeAttributeConverter}
 *
 * @author Juan Rada
 *
 */
public class LocalDateTimeAttributeConverterTest
{
    private LocalDateTimeAttributeConverter testInstance = new LocalDateTimeAttributeConverter();

    @Test
    public void testConvertToDatabaseColumn() throws Exception
    {
        LocalDateTime localDateTime = LocalDateTime.of(2016, 01, 02, 10, 12);
        assertDateTime(localDateTime, testInstance.convertToDatabaseColumn(localDateTime));
    }

    @Test
    public void testConvertToEntityAttribute() throws Exception
    {
        Timestamp timeStamp = new Timestamp(61412569920000l);

        assertDateTime(testInstance.convertToEntityAttribute(timeStamp), timeStamp);
    }


    private void assertDateTime(LocalDateTime localDateTime, Timestamp timestamp)
    {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(timestamp.getTime());

        assertThat(localDateTime.getDayOfMonth(), is(cal.get(Calendar.DAY_OF_MONTH)));
        assertThat(localDateTime.getHour(), is(cal.get(Calendar.HOUR)));
        assertThat(localDateTime.getMinute(), is(cal.get(Calendar.MINUTE)));
        assertThat(localDateTime.getSecond(), is(cal.get(Calendar.SECOND)));
        assertThat(localDateTime.getYear(), is(cal.get(Calendar.YEAR)));
    }
}