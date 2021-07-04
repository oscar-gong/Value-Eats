import React, {useContext } from "react";
import NavBar from "../components/Navbar";
import { MainContent } from "../styles/MainContent";
import { StoreContext } from "../utils/store";
import { Redirect } from "react-router-dom";

export default function EateryLanding() {
  const context = useContext(StoreContext);
  const [auth, setAuth] = context.auth;
  const [isDiner, setIsDiner] = context.isDiner;
  console.log(auth);
  console.log(isDiner);

  if (auth === null) return <Redirect to="/" />;
  if (isDiner === "true") return <Redirect to="/DinerLanding" />;
  console.log(isDiner);

  return (
    <>
      <NavBar isDiner={isDiner}/>
      <MainContent>
          <h1>Eatery page</h1>
      </MainContent>
    </>
  );
}
