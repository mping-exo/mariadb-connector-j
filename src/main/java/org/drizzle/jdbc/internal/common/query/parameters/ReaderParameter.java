/*
 * Drizzle JDBC
 *
 * Copyright (C) 2009 Marcus Eriksson (krummas@gmail.com)
 * All rights reserved.
 *
 * Use and distribution licensed under the BSD license.
 */

package org.drizzle.jdbc.internal.common.query.parameters;

import static org.drizzle.jdbc.internal.common.Utils.needsEscaping;
import org.drizzle.jdbc.internal.common.query.parameters.ParameterHolder;

import java.io.Reader;
import java.io.OutputStream;
import java.io.IOException;

/**
 * User: marcuse
 * Date: Feb 27, 2009
 * Time: 9:35:10 PM
 */
public class ReaderParameter implements ParameterHolder {
    private final long length;
    private final byte[] buffer;
    public ReaderParameter(Reader reader, long length) throws IOException {
        buffer = new byte[(int) (length*2) + 2];
        int pos=0;
        buffer[pos++] = '"';
        for(int i = 0;i<length;i++) {
            byte b = (byte) reader.read();
            if(needsEscaping(b))
                buffer[pos++]='\\';
            buffer[pos++]=b;
        }
        buffer[pos++] = '"';
        this.length = pos;
    }

    public void writeTo(OutputStream os) throws IOException {
        os.write(buffer,0, (int) length);
        os.flush();
    }

    public long length() {
        return length;
    }
}