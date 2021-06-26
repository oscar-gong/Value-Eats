import "./App.css";
import { BrowserRouter as Router, Switch, Route } from "react-router-dom";
import { Main } from "./styles/Main";
import Login from "./pages/Login";
import RegisterDiner from "./pages/RegisterDiner";
import RegisterEatery from "./pages/RegisterEatery";
import DinerLanding from "./pages/DinerLanding";
import EateryLanding from "./pages/EateryLanding";

function App() {
    return (
        <>
            <Main>
                <Router>
                    <Switch>
                        <Route exact path="/dinerLanding">
                            <DinerLanding />
                        </Route>
                        <Route exact path="/">
                            <Login />
                        </Route>
                        <Route exact path="/RegisterDiner">
                            <RegisterDiner />
                        </Route>
                        <Route exact path="/RegisterEatery">
                            <RegisterEatery />
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
