//校验code是否属于某个枚举
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(
    //实际的校验类
    validatedBy = {CNEnumCodeValidator.class}
)
public @interface CNEnumCode {
    String message() default "{com.validation.constraints.CNEnumCode.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    Class<? extends CodeName> type();

    @Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER})
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    public @interface List {
        CNEnumCode[] value();
    }
}
//实现javax.validation.ConstraintValidator 接口
public class CNEnumCodeValidator implements ConstraintValidator<CNEnumCode, Object> {
    private static final Logger LOGGER = Logger.getLogger(CNEnumCodeValidator.class.getName());
    private List<Object> codes;

    public CNEnumCodeValidator() {
    }

    public void initialize(CNEnumCode constraintAnnotation) {
        Class<? extends CodeName> clazz = constraintAnnotation.type();
        if (!clazz.isEnum()) {
            LOGGER.warning("校验类非枚举类型，clazz: " + clazz);
            throw new IllegalArgumentException("校验类非枚举类型");
        } else {
            this.codes = new ArrayList();
            CodeName[] arr$ = (CodeName[])clazz.getEnumConstants();
            int len$ = arr$.length;

            for(int i$ = 0; i$ < len$; ++i$) {
                CodeName codeName = arr$[i$];
                this.codes.add(codeName.getCode());
            }

        }
    }

    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        } else if (value.getClass().isArray()) {
            Object[] values = (Object[])((Object[])value);
            Object[] arr$ = values;
            int len$ = values.length;

            for(int i$ = 0; i$ < len$; ++i$) {
                Object o = arr$[i$];
                if (!this.isValid(o)) {
                    return false;
                }
            }

            return true;
        } else if (Collection.class.isAssignableFrom(value.getClass())) {
            Collection values = (Collection)value;
            Iterator i$ = values.iterator();

            Object o;
            do {
                if (!i$.hasNext()) {
                    return true;
                }

                o = i$.next();
            } while(this.isValid(o));

            return false;
        } else {
            return this.isValid(value);
        }
    }

    private boolean isValid(Object value) {
        return value == null || value.toString().trim().length() == 0 || this.codes.contains(value);
    }
}


//在Spring中的使用方法，添加@Validated注解
@Validated
public class InternalPromotionV2Controller extends BaseController {

}
