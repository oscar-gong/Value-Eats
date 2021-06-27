import React, { useState, createContext } from 'react';
import PropTypes from 'prop-types';

export const StoreContext = createContext(null);

function StoreProvider ({ children }) {
  // variant options for alert are:
  //  - success 
  //  - error
  //  - warning
  //  - info
  const [alertOptions, setAlertOptions] = useState({ showAlert: false, variant: 'success', message: 'message' });

  const store = {
    alert: [alertOptions, setAlertOptions]
  }
  console.log(typeof children);

  return (<StoreContext.Provider value={store}>{children}</StoreContext.Provider>);
}

StoreProvider.propTypes = {
  children: PropTypes.object
}

export default StoreProvider;