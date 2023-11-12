import React from 'react';

import { StyleSheet, View, Button, Alert } from 'react-native';
import { wake, sleep } from 'react-native-websocket';

export default function App() {
  const handleWake = async () => {
    const msg = await wake();

    Alert.alert('Wake', `Result: ${msg}`);
  };

  const handleSleep = async () => {
    const msg = await sleep();

    Alert.alert('Sleep', `Result: ${msg}`);
  };

  return (
    <View style={styles.container}>
      <Button title="Wake Up" onPress={handleWake} />
      <Button title="Sleep" onPress={handleSleep} />
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    alignItems: 'center',
    justifyContent: 'center',
  },
  box: {
    width: 60,
    height: 60,
    marginVertical: 20,
  },
  button: {
    width: 150,
  },
});
