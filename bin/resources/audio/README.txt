Drop .wav files here. AudioSystem will load them on startup.

Required files:
  roar.wav     - played when carnivore catches prey
  eat.wav      - played when herbivore eats plant
  bird.wav     - ambient bird chirp, every 8-16 seconds randomly
  leaves.wav   - leaves rustling, every 0.7 seconds while any land animal is in FOREST

If a file is missing, AudioSystem logs the event to console instead of crashing.

IntelliJ note: ensure src/ is marked as Sources Root and Resource Patterns
includes *.wav (Settings -> Build, Execution, Deployment -> Compiler -> Resource Patterns).
