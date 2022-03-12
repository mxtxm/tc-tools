# قراردادهای Java #
این قوانین و استاندارها برای دست و پاگیر شدن نیست و بری:
* برای خوانایی کد و دردسر کمتر در مراجعات آینده است
* ۸۰ درصد زمان و هزینه نرم افزار صرف نگهداری و بروز رسانی می شود
* به ندرت برنامه نویسان اولیه در تمام مدت نگهدادری و بروز رسانی برروی آن کار خواهند کرد
* پیروی از استاندارها باعث می شوند برنامه نویسان دیگر به سادگی بتوانند کار را پیگیری کنند
* هنگام تحویل یک محصول باید اظمینان داشته باشید که از نظر تمییزی کار خدشه ای به آن وارد نیست

قراردادهای جاوا جامع هستند می توانید از آنها برای کدهای دیگر مانند PHP نیز استفاده کنید.

## فایل ها ##
* فایل ها باید ***utf-8*** باشند
* انتهای خطوط باید ظبق Unix LF (linefeed) باشند یعنی ***\n*** و نه ***\r\n***
* تعداد خطوط کد نباید از ۲۰۰۰ خط تجاوز کند. ایده ال زیر ۱۰۰۰ خط است
* هر فایل باید شامل فقط یک کلاس یا اینترفیس و یا enum باشد (سوای کلاس یا ایترفیس های داخلی کلاس ها)
* بجای پیچیده کرده نام کلاس ها برای طبقه بندی، پروژه می بایست بر اساس منطق و خوانایی package بندی گردد
* بخش های اصلی کد داخل فایل باید با خطوط خالی از هم جدا شوند و یا یک کامنت استاندارد
* در صورت تمایل به قرار دادن کامنتی برای فایل باید در ابتدای فایل و بصورت زیر باشد:

        /*
         * Classname
         *
         * Version info
         *
         * Copyright notice
         */
* در ابتدای فایل (در صورت داشتن کامنت بالا پس از آن) پکیچ و ایمپورت ها تعریف شوند (خطوط خالی)
        
        package .....;
       
        import ......;
        import ......;
        import ......;

        class ....
* تعریف کلاس ها و اینترفیس ها ترتیب زیر را تا حد ممکن رعایت کنید
1. در صورت تمایل (نیازی نیست و پیشنهاد نمی شود) کامت تعریف کلاس
2. تعریف کلاس یا اینترفیس
3. پراپرتی های استاتیک
4. پراپرتی ها instance به ترتیب public protected private
5. constructor
6. متد ها
    * ترتیب متد ها را بر اساس منظقی و خوانایی پشت سر هم قرار دهید نه public protected private
    * هدف آن باشد که مهندس جدید راحت تر با کد آشنا شود


        /**
         * The Example class provides ...
         */
        public class Example {

             private static final String CONST_SOMETHING = "...."; 
            
             public String boo;
             protected String poo;
             private int moo;
             
             
             public Example() {
             
             }

             public void doSomething() {
             
             }
        }


## استایل کد ##
### ترتیب ###
کلملا کلیدی قبل تعریف کلاس، اینترفیس، متد... به ترتیب زیر بیایند:

1. public / private / protected
2. abstract
3. static
4. final
5. transient
6. volatile
7. **default**
8. synchronized
8. native
10. strictfp
### نام گذاری ها ###
* نام کلاس باید اسم باشد نه فعل...
* نام کلاس باید کامل باش نه مخفف مگر مخفف مصطلح مانند URL و HTML
* نام اینترفیس از قوانین کلاس پیروی می کند
* نام متدها باید فعل باشند
* نام متدها باید به درستی انتخاب شوند و بیانگر عملکرد آنها باشند بنابراین بهتر است فعل باشند و یا با get و set شروع شوند
  برخی نام ها به علت خوانایی و مفهوم ممکن اسن فعل انتخاب نشوند
* نام کلاس ها باید بصورت ***StudlyCaps*** باشد
* نام متد ها باید بصورت ***camelCase*** باشد
* نام constant ها باید بصورت ***UPPER_CASE_WITH_UNDER_LINE*** باشد
* متغیرهای محلی و کلاسی باید ***camelCase*** باشد
* نام متغیرها و پراپرتی باید گویای مورد استفاده آنها باشند. استفاده از تک حرف و یا ترکیبات بی معنی و یا مثلا temp و t1 و اینگونه اسامی قابل قبول نیست
    * استقاده از iو j و ... در for و foreach مانعی ندارد
    * گاهی یک متغیر واقعا بی ارزش است و نمی توان نام درستی برای آن پیدا کرد پس نیازی نیست درگیر پیدا کردن نام مناسب شویم، اما این مساله نباید به یک عادت تبدیل شود
### خط ها ###
* برای خوانایی بهتر اس خطوط بلند نباشند مثلا هر خط بیشتر از ۸۰ تا ۱۵۰ کاراکتر نباشد
    * برای شکست خطوط از محلی که به چشم مناسب تر است بشکنید مثلا بعد از کما نقطه یا اپراتور
    * خط جدید زیر بخشی که به آن مربوط است قرار گیرد
    * در صورتی که احساس کردید قوانین بالا خوانا نشد خودتان فاصله گذاری مناسب را انجام دهید

    
        method(longExpression1, longExpression2, longExpression3,
               longExpression4, longExpression5);
        
        String var = function1(longExpression1,
                                       function2(longExpression2,
                                                 longExpression3));
    
        String var = function1(
                longExpression1,
                function2(
                    longExpression2,
                    longExpression3
                )
            );
    
    
    
* در هر خط بیشتر از یک عبارت نباشد
* برای خوانایی بهتر است پیش از هر کلاس دو خط خالی قرار داد
* برای خوانایی بهتر است پیش از هر متد یک خط خالی قرار داد
* برای خوانایی بهتر است پیش از بخش های و بلاک های مختلف کد یک خط خالی قرار داد
### تورفتگی خطوط indenting ###
* باید به SPACE باشد و نه TAB
* باید مضربی از ۴ SPACE باشد و نه کمتر و بیشتر

### کامنت ها ###
* کد باید خوانا باشد و بدون کامنت مخصوصا کامنت هایی که بدیهیات را توضیح می دهند
* در صورت تشخص نیاز به کامنت بالای خط یا بلاک مذکور باشد و نباید در انتهای خط کد باشد
* تنها کامت هایی که مجاز هستند:
    * توضیح در ابتدای فایل
    * شرح کلاس
    * شرح متد
    * باید طبق استانداردهای این صفحه باشند: 
      https://docs.phpdoc.org/latest/guides/docblocks.html
### بلاک ها ###
* کد های بلاک هایی مانند if و لوپ ها باید حتما در }{ قرار گیرند
 و نباید تک خطی باشند
* باید از استایل K&R پیروی گردد

K&R

    publi class ClassName {
        
        public void methodName() {
            int x = 2;
            if (x == 1) {
                // do something ...
            } else {
                // do something ...
            }
        }
    }
    
### اعضای کلاس ها ###
* تمام متدها و propertyها باید visibility (public, protected, private) داشته باشند
* نباید نام اعضای کلاس را با underscore ـ شروع کرد مثلا برای نشان دادن privateها، استفاده از underscore هیج مفهومی ندارد
* هنگام استفاده از propertyها فاصله ها باید بصورت زیر باشد:  
  int counter = 1;
* هنگام تعریف methodها فاصله ها باید بصورت زیر باشد:  
  public method(String key, int value) { ...
* هنگام فراخوانی methodها فاصله ها باید بصورت زیر باشد:  
  obj.method("value", 34);
* پراپرتی های کلاس بصورت خودبخود مقداردهی اولی می شوند و نیاز به مقداردهی پیش فرض اولیه ندارند مگر مقداری غیر از پیش فرض نیاز باشد

        private String foo; // === private String foo = null;   
        private Long foo; // === private Long foo = null;   
        private Boolean foo; // === private Boolean foo = null;   
        private int foo; // === private int foo = 0;   
        private long foo; // === private int foo = 0L;   
        private double foo; // === private int foo = 0.0;   
        private bool foo; // === private bool foo = false;   
  
مثال، اسپیس ها و دندانه ها و خطوط باید مانند زیر رعایت شوند:

    public class ClassName {
     
        private static final String CONST_SOMETHING = "...."; 
        private int foo;
        
        public String doSomething() {
        
        }
        
        public void fooBarBaz(long arg1, String... arg2) {

        }
    }
    
### بلاک ها کنترل ها if, for, foreach, ... ###
* بین نام بلاک و پرانتز یک اسپیس باشد:

        if (something == somethingElse) {

* پرهیز از تک خطی. همیشه باید بلاک تعریف شود حتی اگر داخل آن یک خط داریم:
        
        // good
        if (something == somethingElse) {
            return true;
        }
        
        // bad
        if (something == somethingElse) return true;

        // bad
        if (something == somethingElse)
            return true;
        
* if خلاصه

        String value = $something == somethingElse ? "yes" + " it is true" : "no";
          
        alpha = (aLongBooleanExpression)
            ? beta
            : gamma;
            
### خوب و بد ###
* متد استاتیک با اسم کلاس فراخوانی گردد نه اسم متغییر instance آن
* از مقدایر عددی و رشته ای متغییر مستقیما در کد استفاده نکنید. بهتر است constant شوند

        d = (a = b + c) + r;        // bad

        // bad
        if (booleanExpression) {
            return true;
        } else {
            return false;
        }
        
        // good
        return booleanExpression;
        
        // bad
        if (condition) {
            return x;
        }
        return y;
        
        // good
        return condition ? x : y;



