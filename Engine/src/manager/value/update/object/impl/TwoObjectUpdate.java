package manager.value.update.object.impl;

import manager.value.update.object.api.UpdateObject;

public class TwoObjectUpdate implements UpdateObject {
    private Object objectForUpdate1;
    private Object getObjectForUpdate2;

    public TwoObjectUpdate(Object objectForUpdate1, Object getObjectForUpdate2) {
        this.objectForUpdate1 = objectForUpdate1;
        this.getObjectForUpdate2 = getObjectForUpdate2;
    }

    public Object getObjectForUpdate1() {
        return objectForUpdate1;
    }

    public Object getGetObjectForUpdate2() {
        return getObjectForUpdate2;
    }
}
