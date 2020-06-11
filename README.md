# fsynth

A 4 part synth using Web Audio API and Clojurescript.  Just a toy experiment.

![4 parts](https://github.com/fedreg/fsynth/blob/master/resources/preview1.png?raw=true)
![part editor view](https://github.com/fedreg/fsynth/blob/master/resources/preview2.png?raw=true)

## NOTE: Comment out this line in `core.cljs` to increase performance
```clj
:filter (note-brightness on? state pos)

```
### Development mode
To start the Figwheel compiler, navigate to the project folder and run the following command in the terminal:

```
lein figwheel
```

Figwheel will automatically push cljs changes to the browser.
Once Figwheel starts up, you should be able to open the `public/index.html` page in the browser.

### REPL

The project is setup to start nREPL on port `7002` once Figwheel starts.
Once you connect to the nREPL, run `(cljs)` to switch to the ClojureScript REPL.

### Building for production

```
lein clean
lein package
```
