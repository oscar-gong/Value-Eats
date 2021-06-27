import React, { useState, useContext } from 'react';
import "./App.css";
import { BrowserRouter as Router, Switch, Route } from "react-router-dom";
import { Main } from "./styles/Main";
import Login from "./pages/Login";
import RegisterDiner from "./pages/RegisterDiner";
import RegisterEatery from "./pages/RegisterEatery";
import DinerLanding from "./pages/DinerLanding";
import EateryLanding from "./pages/EateryLanding";
import { Snackbar } from '@material-ui/core';
import { Alert } from '@material-ui/lab';
import { StoreContext } from './utils/store';

function App() {

  const context = useContext(StoreContext);
  console.log(context);
  const [alertOptions, setAlertOptions] = context.alert;
  const [token, setToken] = useState("");

  return (
    <>
      <Snackbar open={alertOptions.showAlert} autoHideDuration={6000} onClose={() => setAlertOptions({ ...alertOptions, showAlert: false })}>
        <Alert onClose={() => setAlertOptions({ ...alertOptions, showAlert: false })} severity={alertOptions.variant}>
          {alertOptions.message}
        </Alert>
      </Snackbar>
      <Main>
        <Router>
          <Switch>
          <Route exact path="/dinerLanding">
              <DinerLanding token={token}/>
            </Route>
            <Route exact path="/">
              <Login setToken={setToken}/>
            </Route>
            <Route exact path="/RegisterDiner">
              <RegisterDiner setToken={setToken}/>
            </Route>
            <Route exact path="/RegisterEatery">
              <RegisterEatery setToken={setToken}/>
            </Route>
            <Route exact path="/EateryLanding">
              <EateryLanding />
            </Route>
          </Switch>
        </Router>
      </Main>
    </>
  );
}

export default App;
