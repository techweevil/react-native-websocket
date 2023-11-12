import Websocket from './Websocket';

export async function wake(): Promise<string> {
  return Websocket.wake();
}

export async function sleep(): Promise<string> {
  return Websocket.sleep();
}

export async function multiply(a: number, b: number): Promise<number> {
  return Websocket.multiply(a, b);
}
