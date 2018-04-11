package Interfaces;

public interface IPluginDescriptor {
    public String getName();
    public String getJarPath();
    public String getClassName();
    public String getDescription();

    public void setName(String name);
    public void setJarPath(String jarpath);
    public void setClassName(String classname);
    public void setDescription(String description);
}
