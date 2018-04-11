package Plugin;

import Interfaces.IPluginDescriptor;

public class PluginDescriptor implements IPluginDescriptor {

    private String name;
    private String jarFileName;
    private String className;
    private String description;

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getJarPath() {
        return jarFileName;
    }

    @Override
    public String getClassName() {
        return className;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public void setName(String name) {
        this.name= name;
    }

    @Override
    public void setJarPath(String jarpath) {
        this.jarFileName= jarpath;
    }

    @Override
    public void setClassName(String classname) {
        this.className= classname;
    }

    @Override
    public void setDescription(String description) {
        this.description= description;
    }
}
