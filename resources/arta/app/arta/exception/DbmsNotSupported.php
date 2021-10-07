<?php
/**
 * Artaengine project http://artaengine.com/
 * ::COPYRIGHT2009::
 * 
 * ::LICENCE::
 * ::LICENCE-URL::
 *
 * ClassID  7904
 * Created  2009/01/27
 * Updated  2013/02/05
 */

//namespace arta\exception;

/**
 * Database engine is not supported. Feel free to contribute an IDbAbstract and
 * IDbAdvanced implementation for it :)
 *
 * @copyright  ::COPYRIGHT2009::
 * @author     Mehdi Torabi <mehdi @ artaengine.com>
 * @version    1.0.0
 * @since      1.4.0
 */
class DbmsNotSupported extends Exception {

    protected $code = 79041;

    public function __construct($dbms) {
        $this->message = 'Database engine "'.$dbms.'" is not supported by this version of Artaengine.';
    }
}
