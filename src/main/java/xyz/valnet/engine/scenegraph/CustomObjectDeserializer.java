package xyz.valnet.engine.scenegraph;

import java.io.IOException;
import java.io.InputStream;
import java.io.InvalidClassException;
import java.io.ObjectInputStream;
import java.io.ObjectStreamClass;

import xyz.valnet.hadean.gameobjects.ui.tabs.DebugTab;
 
public class CustomObjectDeserializer extends ObjectInputStream {
 
 
    public CustomObjectDeserializer(InputStream in) throws IOException {
        super(in);
    }
 
    @SuppressWarnings("rawtypes")
    protected ObjectStreamClass readClassDescriptor() throws IOException, ClassNotFoundException {
        ObjectStreamClass resultClassDescriptor = super.readClassDescriptor(); // initially streams descriptor
        Class localClass; // the class in the local JVM that this descriptor represents.
        try {
            localClass = Class.forName(resultClassDescriptor.getName());
        } catch (ClassNotFoundException e) {
            DebugTab.log("No local class for " + resultClassDescriptor.getName());
            DebugTab.log(e);
            return resultClassDescriptor;
        }
        ObjectStreamClass localClassDescriptor = ObjectStreamClass.lookup(localClass);
        if (localClassDescriptor != null) { // only if class implements serializable
            final long localSUID = localClassDescriptor.getSerialVersionUID();
            final long streamSUID = resultClassDescriptor.getSerialVersionUID();
            if (streamSUID != localSUID) { // check for serialVersionUID mismatch.
                final StringBuffer s = new StringBuffer("Overriding serialized class version mismatch: ");
                s.append("local serialVersionUID = ").append(localSUID);
                s.append(" stream serialVersionUID = ").append(streamSUID);
                Exception e = new InvalidClassException(s.toString());
                DebugTab.log("Potentially Fatal Deserialization Operation.");
                DebugTab.log(e);
                resultClassDescriptor = localClassDescriptor; // Use local class descriptor for deserialization
            }
        }
        return resultClassDescriptor;
    }
}