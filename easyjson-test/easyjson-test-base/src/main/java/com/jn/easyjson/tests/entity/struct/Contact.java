/*
 * Copyright 2019 the original author or authors.
 *
 * Licensed under the Apache, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at  http://www.gnu.org/licenses/lgpl-3.0.html
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.jn.easyjson.tests.entity.struct;

public class Contact {
    private String email;
    private String phone;
    private String mobilePhone;
    private String qq;
    private String msn;
    private String webchat;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }

    public String getMsn() {
        return msn;
    }

    public void setMsn(String msn) {
        this.msn = msn;
    }

    public String getWebchat() {
        return webchat;
    }

    public void setWebchat(String webchat) {
        this.webchat = webchat;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Contact contact = (Contact) o;

        if (!email.equals(contact.email)) {
            return false;
        }
        if (phone != null ? !phone.equals(contact.phone) : contact.phone != null) {
            return false;
        }
        if (mobilePhone != null ? !mobilePhone.equals(contact.mobilePhone) : contact.mobilePhone != null) {
            return false;
        }
        if (qq != null ? !qq.equals(contact.qq) : contact.qq != null) {
            return false;
        }
        if (msn != null ? !msn.equals(contact.msn) : contact.msn != null) {
            return false;
        }
        return webchat != null ? webchat.equals(contact.webchat) : contact.webchat == null;
    }

    @Override
    public int hashCode() {
        int result = email.hashCode();
        result = 31 * result + (phone != null ? phone.hashCode() : 0);
        result = 31 * result + (mobilePhone != null ? mobilePhone.hashCode() : 0);
        result = 31 * result + (qq != null ? qq.hashCode() : 0);
        result = 31 * result + (msn != null ? msn.hashCode() : 0);
        result = 31 * result + (webchat != null ? webchat.hashCode() : 0);
        return result;
    }
}
