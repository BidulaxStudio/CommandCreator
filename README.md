# CommandCreator
A Minecraft plugin that allows you to create new commands based on other commands

### Commands
- /cc help : Shows help message
- /cc use \<name> or \#\<name> : Executes a personalized command.
- /cc add \<name> \<permission> \<command> : Creates a new command.
- /cc remove \<name> : Removes a command.
- /cc list : Lists all the personalized commands.

### Permissions
- commandcreator.add : Can access to /cc add
- commandcreator.remove : Can access to /cc remove
- commandcreator.get : Can access to /cc list
- commandcreator.use : Can use all the commands

Thanks to this plugin, you can create any command based on another command.
<br>For example, you can type /cc add trycc op say I tryed CommandCreator !
<br>Then, type #trycc in the chat, and you'll see the result...