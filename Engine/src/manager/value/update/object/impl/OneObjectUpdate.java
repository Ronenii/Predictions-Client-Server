package manager.value.update.object.impl;

import manager.value.update.object.api.UpdateObject;

public class OneObjectUpdate implements UpdateObject {
    private Object objectForUpdate;

    public OneObjectUpdate(Object objectForUpdate) {
        this.objectForUpdate = objectForUpdate;
    }

    public Object getObjectForUpdate() {
        return objectForUpdate;
    }
}
