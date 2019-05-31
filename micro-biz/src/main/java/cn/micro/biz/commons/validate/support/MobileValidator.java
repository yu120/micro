package cn.micro.biz.commons.validate.support;

import cn.micro.biz.commons.validate.Mobile;
import lombok.extern.slf4j.Slf4j;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

/**
 * The mobile phone Constraint Validator
 *
 * @author lry
 */
@Slf4j
public class MobileValidator implements ConstraintValidator<Mobile, String> {

    /**
     * 验证固话号码
     */
    private static final Pattern TEL_PATTERN = Pattern.compile("^(0\\d{2}-\\d{8}(-\\d{1,4})?)|(0\\d{3}-\\d{7,8}(-\\d{1,4})?)$");
    /**
     * 正则：手机号（精确）
     * <p>移动：134(0-8)、135、136、137、138、139、147、150、151、152、157、158、159、178、182、183、184、187、188、198</p>
     * <p>联通：130、131、132、145、155、156、175、176、185、186、166</p>
     * <p>电信：133、153、173、177、180、181、189、199</p>
     * <p>全球星：1349</p>
     * <p>虚拟运营商：170</p>
     */
    private static final Pattern MOBILE_PATTERN = Pattern.compile("^(13[0-9]|14[579]|15[0-3,5-9]|16[6]|17[0135678]|18[0-9]|19[89])\\d{8}$");

    private Mobile mobile;

    @Override
    public void initialize(Mobile mobile) {
        this.mobile = mobile;
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (mobile.allowNull()) {
            if (value == null || value.length() == 0) {
                return true;
            }

            return validateMobile(value);
        }

        return validateMobile(value);
    }

    private boolean validateMobile(String value) {
        return mobile.tel() ? TEL_PATTERN.matcher(value).matches() : MOBILE_PATTERN.matcher(value).matches();
    }

}