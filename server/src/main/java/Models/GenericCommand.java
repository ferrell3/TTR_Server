package Models;

import java.lang.reflect.Method;
import Interfaces.ICommand;

public class GenericCommand implements ICommand {
    private String _className;
    private String _methodName;
    private Class<?>[] _paramTypes;
    private Object[] _paramValues;

    @Override
    public String execute(){
        //TODO: Finish this, doesn't work and doesn't return anything
        String str = "";
        try {
            Class<?> receiver = Class.forName(_className);
            Method method = receiver.getMethod(_methodName, _paramTypes);
            method.invoke(null, _paramValues);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return str;
    }
//    @Override
//    public Results execute(){
//        Results result = new Results();
//        try {
//            Class<?> receiver = Class.forName(_className);
//            Method method = receiver.getMethod(_methodName, _paramTypes);
//            method.invoke(null, _paramValues);
//        }
//        catch (Exception e) {
//            e.printStackTrace();
//        }
//        return result;
//    }
}

