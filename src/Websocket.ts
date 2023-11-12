import { NativeModules, Platform } from 'react-native';

const LINKING_ERROR =
  `The package 'react-native-websocket' doesn't seem to be linked. Make sure: \n\n` +
  Platform.select({ ios: "- You have run 'pod install'\n", default: '' }) +
  '- You rebuilt the app after installing the package\n' +
  '- You are not using Expo Go\n';

const Websocket = NativeModules.Websocket
  ? NativeModules.Websocket
  : new Proxy(
      {},
      {
        get() {
          throw new Error(LINKING_ERROR);
        },
      }
    );

interface WebsocketInterface {
  multiply(a: number, b: number): Promise<number>;
  wake(): Promise<string>;
  sleep(): Promise<string>;
}

export default Websocket as WebsocketInterface;
