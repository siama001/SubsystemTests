package SubsystemTests;

import java.util.Enumeration;
import java.util.HashMap;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionContext;

public class StubSession implements HttpSession{

        private HashMap<String, Object> map = new HashMap<String, Object>();
        private String currentURL;
        public boolean sessionInvalidated =false;
        
        public void setURL(String url){
        	currentURL = url;
        }
        
        public String getURL(){
        	return currentURL;
        }
      
        //@Override
        public void setAttribute(String arg0, Object arg1) {
                map.put(arg0, arg1);
        }

        //@Override
        public Object getAttribute(String arg0) {
                return map.get(arg0);
        }

        //@Override
        public Enumeration getAttributeNames() {
                // TODO Auto-generated method stub
                return null;
        }

        //@Override
        public long getCreationTime() {
                // TODO Auto-generated method stub
                return 0;
        }

        //@Override
        public String getId() {
                // TODO Auto-generated method stub
                return null;
        }

        //@Override
        public long getLastAccessedTime() {
                // TODO Auto-generated method stub
                return 0;
        }

        //@Override
        public int getMaxInactiveInterval() {
                // TODO Auto-generated method stub
                return 0;
        }

        //@Override
        public ServletContext getServletContext() {
                // TODO Auto-generated method stub
                return null;
        }

        @SuppressWarnings("deprecation")
        //@Override
        public HttpSessionContext getSessionContext() {
                // TODO Auto-generated method stub
                return null;
        }

        //@Override
        public Object getValue(String arg0) {
                // TODO Auto-generated method stub
                return null;
        }

        //@Override
        public String[] getValueNames() {
                // TODO Auto-generated method stub
                return null;
        }

        //@Override
        public void invalidate() {
        	sessionInvalidated = true;

        }

        //@Override
        public boolean isNew() {
                // TODO Auto-generated method stub
                return false;
        }

        //@Override
        public void putValue(String arg0, Object arg1) {
                // TODO Auto-generated method stub

        }

        //@Override
        public void removeAttribute(String arg0) {
                // TODO Auto-generated method stub

        }

        //@Override
        public void removeValue(String arg0) {
                // TODO Auto-generated method stub

        }

        //@Override
        public void setMaxInactiveInterval(int arg0) {
                // TODO Auto-generated method stub

        }

}
