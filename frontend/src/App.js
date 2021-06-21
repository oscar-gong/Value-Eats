import './App.css';
import {
  BrowserRouter as Router,
  Switch,
  Route
} from 'react-router-dom';
import { Main } from './styles/Main';
import Login from './pages/Login';

function App() {
  return (
    <>
      <Main>
        <Router>
          <Switch>
            <Route path="/">
              <Login/>
            </Route>
          </Switch>
        </Router>
      </Main>
    </>
  );
}

export default App;
