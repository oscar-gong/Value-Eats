import './App.css';
import {
  BrowserRouter as Router,
  Switch,
  Route
} from 'react-router-dom';
import { Main } from './styles/Main';
import Login from './pages/Login';
import RegisterDiner from './pages/RegisterDiner';
import RegisterEatery from './pages/RegisterEatery';

function App() {
  return (
    <>
      <Main>
        <Router>
          <Switch>
            <Route exact path="/">
              <Login/>
            </Route>
            <Route exact path="/RegisterDiner">
              <RegisterDiner/>
            </Route>
            <Route exact path="/RegisterEatery">
              <RegisterEatery/>
            </Route>
          </Switch>
        </Router>
      </Main>
    </>
  );
}

export default App;
