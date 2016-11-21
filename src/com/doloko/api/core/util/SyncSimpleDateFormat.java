package com.doloko.api.core.util;

import java.text.FieldPosition;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/*
 * <p>Title: EAP企业应用开发平台</p>
 *
 * <p>Description: 旨在为各位同仁提供统一的基础开发平台，提高开发效率，改进工作质量！</p>
 *
 * <p>Copyright: Copyright (C) Surekam 2008</p>
 *
 * <p>Company:  </p>
 */

/**
 * <p> 描述: 线程安全的DateFormat</p>
 * <p>
 * <p> Create Date: 2009-10-13  16:14:55 <p>
 *
 * @author betafox
 * @version 1.0
 */
public class SyncSimpleDateFormat extends SimpleDateFormat {
    SyncSimpleDateFormat(String pattern) {
        super(pattern);
    }

    @Override
    public synchronized Date parse(String source) throws ParseException {
        return super.parse(source);
    }

    @Override
    public synchronized StringBuffer format(Date date, StringBuffer toAppendTo, FieldPosition pos) {
        return super.format(date, toAppendTo, pos);
    }
}
