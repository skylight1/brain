Prerequisite:
For now use a Settings.apk or BluetoothManager.apk (easier) to pair the brainwave headset.

Using BluetoothManager.apk:
The list shows paired (highlighted) and unpaired devices 
Clicking on a paired device unpairs it so be careful not to unpair your phone from Glass.


TODO Tasks:

- add option to read values from a file instead of the brainwave headset
This is to allow development without a brainwave headset (record values and playback)

- deprecate BrainActivity -> migrate to live card -> BrainService, TrainBrainDrawer
(currently doesn't run in the background or as a live card) - update manifest!

- make it draw lines instead of text values
see WaveForm sample project for drawing lines on canvas
i.e. draw Attention and Meditation values over time (try different colors)

This initial activity is to practice training the brain using attention and meditation values.
Let's start off by training for 4 possible combinations: 
both on high levels (eventually a user selectable threshold)
both on low levels
both on opposite levels (low and high and vice versa)

- Using an additional activity, user selects which ones will run corresponding "commands"
as well as defining the commands (e.g. wake up Glass or swipe left/right)

- Commands are then programmed to be triggered from the background service.

- add blink values to the mix to add more combinations

