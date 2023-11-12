import Websocket from './Websocket';

export async function wake(): Promise<string> {
  return Websocket.wake();
}

export async function sleep(): Promise<string> {
  return Websocket.sleep();
}
