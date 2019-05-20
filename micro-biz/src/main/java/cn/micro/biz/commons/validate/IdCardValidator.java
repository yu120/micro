package cn.micro.biz.commons.validate;

import lombok.extern.slf4j.Slf4j;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * The IdCard phone Constraint Validator
 *
 * @author lry
 */
@Slf4j
public class IdCardValidator implements ConstraintValidator<IdCard, String> {

    private final static int[] POWER = {7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2};
    private final static int[] VERIFY_CODE = {'1', '0', 'X', '9', '8', '7', '6', '5', '4', '3', '2'};
    private final static List<String> CITY_CODE = Arrays.asList("11", "12", "13", "14", "15", "21", "22",
            "23", "31", "32", "33", "34", "35", "36", "37", "41", "42", "43", "44", "45", "46",
            "50", "51", "52", "53", "54", "61", "62", "63", "64", "65", "71", "81", "82", "91");

    private IdCard idCard;

    @Override
    public void initialize(IdCard idCard) {
        this.idCard = idCard;
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (idCard.allowNull()) {
            if (value == null || value.length() == 0) {
                return true;
            }

            return isIdCard(value);
        }

        return isIdCard(value);
    }


    /**
     * 身份证验证
     *
     * @param idCard 号码内容
     * @return 是否有效 null和"" 都是false
     */
    private static boolean isIdCard(String idCard) {
        if (idCard == null || (idCard.length() != 15 && idCard.length() != 18)) {
            return false;
        }

        final char[] cs = idCard.toUpperCase().toCharArray();
        // 校验位数
        int power = 0;
        for (int i = 0; i < cs.length; i++) {
            if (i == cs.length - 1 && cs[i] == 'X') {
                //最后一位可以 是X或x
                break;
            }
            if (cs[i] < '0' || cs[i] > '9') {
                return false;
            }
            if (i < cs.length - 1) {
                power += (cs[i] - '0') * POWER[i];
            }
        }

        // 校验区位码
        if (!CITY_CODE.contains(idCard.substring(0, 2))) {
            return false;
        }

        // 校验年份
        String year;
        try {
            year = idCard.length() == 15 ? getIdCardCalendar(idCard) : idCard.substring(6, 10);
        } catch (Exception e) {
            throw new IllegalArgumentException("Illegal IdCard");
        }
        final int iYear = Integer.parseInt(year);
        if (iYear < 1900 || iYear > Calendar.getInstance().get(Calendar.YEAR)) {
            // 1900年的PASS，超过今年的PASS
            return false;
        }

        // 校验月份
        String month = idCard.length() == 15 ? idCard.substring(8, 10) : idCard.substring(10, 12);
        final int iMonth = Integer.parseInt(month);
        if (iMonth < 1 || iMonth > 12) {
            return false;
        }

        // 校验天数
        String day = idCard.length() == 15 ? idCard.substring(10, 12) : idCard.substring(12, 14);
        final int iDay = Integer.parseInt(day);
        if (iDay < 1 || iDay > 31) {
            return false;
        }

        // 校验"校验码"
        if (idCard.length() == 15) {
            return true;
        }

        return cs[cs.length - 1] == VERIFY_CODE[power % 11];
    }

    /**
     * 获取出生年月日
     *
     * @param idCard id card
     * @return year
     */
    private static String getIdCardCalendar(String idCard) throws ParseException {
        Calendar cDay = Calendar.getInstance();
        cDay.setTime(new SimpleDateFormat("yyMMdd").parse(idCard.substring(6, 12)));
        return String.valueOf(cDay.get(Calendar.YEAR));
    }

}