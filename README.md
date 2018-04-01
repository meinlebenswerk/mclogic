# mclogic
An experimental logic to Minecraft-Redstone circuit compiler, currently does not work ;)

## how it works
mclogic takes in .gll files (their syntax is a mixture between Verilog and VHDL and maybe others; a tutorial on how they work will follow once the project has reached a state where it can be regarded as "working"; the Idea is to eventually make it understand Verilog and be able to parse that) and compiles them to working Minecraft-logic circuits.
Basically the gll file gets parsed into a net- and gatelist which then in turn gets translated into minecraft logic structures which then get placed and routed (which is the part that's currently WIP). 
