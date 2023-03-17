# Quarter End Summary for STEM 298
**Initial Tasks**

At the beginning of the quarter, I planned to develop and maintain a Discord bot. Using the Eclipse IDE, Apache Maven, the Java Discord API, Git, and GitHub, I was able to not only create the bot and some of its simple operations, but was also able to practice using software development tools that one may see regularly on the job.

The first few tasks I had in mind were initially setting up the bot, setting up configuration messages, a welcome message to members who join the guild, an announcement message and command for administrators, a custom help and info command, and finally, reaction roles. Out of these six tasks, I was able to complete the first five. 

<img width="813" alt="image" src="https://user-images.githubusercontent.com/107907699/225810574-f98b8df8-4c22-4ed7-9000-8ef481d626e1.png">

**Above:** Bocchi inquiring an administrator (me) about configuring channels

I did note that if I ever got to the point where I had already completed these tasks, I would move on to moderation tools such as a warning command, mute command, and ban command; however, I did not work at a fast enough pace to reach those goals. Those features will be released in the future.

<img width="686" alt="image" src="https://user-images.githubusercontent.com/107907699/225810883-b4749f74-f685-4154-9bd3-f677bc1bde49.png">

**Above:** Bocchi sending help information to the user via a direct message

## List of All Planned Tasks

- [x] **When the bot joins a guild**, inquire an administrator to set an administrator channel, announcements channel, and welcome channel that the bot can access (see `InitialConfiguration.java`).
- [x] **When a user joins a guild** that the bot has access to, send a greeting to the user through the specified welcome channel (see `GeneralListener.java`).

<img width="277" alt="image" src="https://user-images.githubusercontent.com/107907699/225811087-587db4ea-00e4-4ad3-aceb-fd9b09dc004c.png">

**Above:** Bocchi greeting a new member to the server

- [x] **When an administrator sends an announcement command**, send a message to the announcement channel (see `Announcement.java`).
- [x] **When a user sends a help command**, send a private message to the user with the usage instructions for each command (see `HelpCommand.java`). Note that this implementation was taken from [Austin Keener's Yui Bot](https://github.com/DV8FromTheWorld/Yui/blob/5ef26d486c55e4f7bd4ebceb96345a12dca2adc0/src/main/java/net/dv8tion/discord/commands/HelpCommand.java).
- [x] **When a user sends an info command**, send information about the bot to the channel the user sent the message in (see `InfoCommand.java`). Note that this implementation was taken from [Austin Keener's Yui Bot](https://github.com/DV8FromTheWorld/Yui/blob/5ef26d486c55e4f7bd4ebceb96345a12dca2adc0/src/main/java/net/dv8tion/discord/commands/InfoCommand.java).
- [ ] **When a user reacts to a reactable message with a button**, perform some kind of action or behavior (this will mainly be used for reaction roles). This is planned for version `1.1.x`.
- [ ] **When a moderator sends a warning, mute, or ban command**, initiate the desired action upon the specified member of the guild. This is planned for version `1.1.x`.
- [ ] **When an administrator sets a weekly announcement and reminder**, send that announcement on the specified date and time. Note that this feature may not appear anytime soon since this requires hosting the bot on a virtual machine in order to allow it to have 24/7 uptime. I am not willing to spend money to host the bot (yet).

## About OperationsAbstractFactory, BocchiOperations, and GuildInfoLoader

Around the middle of the quarter, when I was still developing Bocchi, I encountered a major issue related to creating uniform messages for the bot. I didn't want to have to specify the same parameters over and over again in my code, and I didn't want to have to create the same method in different scripts either. This is where I used a design pattern to overcome this obstacle: the Singleton pattern. BocchiOperations and GuildInfoLoader are both static classes instantiated at the beginning of runtime. This allows any of my scripts to call the Singleton instance and use the methods described in there rather than redoing all of the code in every script.

In addition to the Singleton class, I also developed an AbstractFactory. This isn't used as much, but since I do plan on developing a personal bot for more experimentation, I decided to create an AbstractFactory in order to bridge those operations over to my other bot in the future.

I surely did not think I would be implementating a design pattern for this project, but it turned out to help me a ton. It also helped me to understand the creation process for other APIs; how the logger I was using, logback classic, also had factory methods to use for object creation.

## Tips for Future STEM 298 Students

In general, I feel that this "class" isn't really a class. I thought of it more as designated work time which really helped me figure things out independently with check-ups every week. For future CS students looking to build their own bot, I really recommend only starting so after you have mastered the logic behind Java or whatever programming language you choose. The next step after learning those languages is to apply them within a framework, which I think is akin to learning a new game. Every new framework you work in, you must learn the rules and strategies to successfully allow your application to perform the tasks you want it to perform. Thinking in gaming terms, it's kind of like the programming "META", or "most effective techniques available".

Working in JDA has been very tiresome, but at the same time, I feel that it has been *very* rewarding for me to learn this framework. There are still a ton of things I need to optimize in my code, and that takes a lot of research into not only JDA, but Java and computer science as a whole. Overall, I'm very excited to continue maintaining and improving my Discord bot and my programming skills.

I think my biggest takeaway from this project as a whole is that software development doesn't just stop after you create the project. There's still maintaining the bot, responding to bugs, and optimizing code for the future.

## Summary

To conclude, in this project, I developed a Discord bot using the JDA. With my bot set up, it can perform simple tasks such as configure the admin, announcements, and welcome channel for a guild, send an announcement and welcome message, and respond to a help and info command. In addition, I implemented the Singleton design pattern as well as an AbstractFactory, and I was able to practice using multiple different tools at my disposal, such as Eclipse, Maven, Git, and GitHub. 
