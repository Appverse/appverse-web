package org.appverse.web.framework.backend.persistence.model.integration;

import org.appverse.web.framework.backend.api.model.integration.AbstractIntegrationBean;


import javax.persistence.MappedSuperclass;
import java.lang.Object;
import java.lang.Override;
import java.lang.Throwable;

@MappedSuperclass
public class AbstractIntegrationJPABean extends AbstractIntegrationBean{

        private static final long serialVersionUID = -2070164067618480119L;
        protected long id;

        // Always add condition "if (id == 0 || id != other.id)" so in case that
        // Dozer non-cummulative collections and remove-orphans in mappings works
        // fine
        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            AbstractIntegrationJPABean other = (AbstractIntegrationJPABean) obj;
            if (id == 0 || id != other.id) {
                return false;
            }
            return true;
        }

        @Override
        public void finalize() throws Throwable {

        }

        // Required so that Dozer non-cummulative collections and remove-orphans in
        // mappings works fine
        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + (int) (id ^ id >>> 32);
            return result;
        }

        public void setId(long id) {
            this.id = id;
        }

}
