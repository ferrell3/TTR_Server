package Interfaces;

public interface ICommand {
    //We will have to tweak a couple things for this.
    //We need to make sure that everything returns a String. Or we can make a Result object
    String execute();
}
