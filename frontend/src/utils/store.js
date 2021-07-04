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

  // get stored token
  const getToken = localStorage.getItem('token');
  const getIsDiner = localStorage.getItem('isDiner');
  
  if (getToken === null) console.log("no token was found from storage");
  if (getIsDiner === null) console.log("no diner state was stored");

  const [isDiner, setIsDiner] = React.useState(getIsDiner);
  const [auth, setAuth] = React.useState(getToken);

  const store = {
    alert: [alertOptions, setAlertOptions],
    isDiner: [isDiner, setIsDiner],
    auth: [auth, setAuth]
  }
  console.log(typeof children);




  return (<StoreContext.Provider value={store}>{children}</StoreContext.Provider>);
}

StoreProvider.propTypes = {
  children: PropTypes.object
}

export default StoreProvider;