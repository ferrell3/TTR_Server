package Models;

import java.lang.reflect.Method;
import Interfaces.ICommand;
import Services.ClientProxy;
import Services.LobbyServices;
import Services.UserServices;

public class Command implements ICommand {
    private String _className;
    private String _methodName;
    private Class<?>[] _paramTypes;
    private String[] _strParamTypes;
    private Request[] _paramValues;

    public Command(String className, String methodName,
                   String[] paramTypes, Request[] paramValues) {
        _className = className;
        _methodName = methodName;
        _strParamTypes = paramTypes;
        _paramValues = paramValues;
    }

    @Override
    public Result execute(){
        setParamTypes();
        Result result = new Result();
        try {
            Class<?> receiver = Class.forName(_className);
            Method method = receiver.getMethod(_methodName, _paramTypes);
            if(_className.equals("Interfaces.IServerUser"))
            {
                result = (Result) method.invoke(UserServices.getInstance(), _paramValues);
            }
            else if(_className.equals("Interfaces.ILobby"))
            {
                result = (Result) method.invoke(LobbyServices.getInstance(), _paramValues);
            }
            else //if(_className.equals("Interfaces.IClient"))
            {
                result = (Result) method.invoke(ClientProxy.getInstance(), _paramValues);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    private void setParamTypes()
    {
        _paramTypes = new Class<?>[_strParamTypes.length];
        for(int i = 0; i < _strParamTypes.length; i++)
        {
            try{
                _paramTypes[i] = (Class.forName(_strParamTypes[i]));
            }catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }
}
