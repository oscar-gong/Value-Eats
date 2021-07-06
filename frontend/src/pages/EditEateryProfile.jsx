import React, {useContext } from "react";
import NavBar from "../components/Navbar";
import { MainContent } from "../styles/MainContent";
import { StoreContext } from "../utils/store";
import { Redirect } from "react-router-dom";

export default function EditEateryLanding() {
  const context = useContext(StoreContext);
  const [auth, setAuth] = context.auth;
  const [isDiner, setIsDiner] = context.isDiner;

  if (auth === null) return <Redirect to="/" />;
  if (isDiner === "true") return <Redirect to="/DinerLanding" />;

  return (
    <>
      <NavBar isDiner={isDiner}/>
      <MainContent>
          <h1>Edit eatery page</h1>
          
      </MainContent>
    </>
  );
}
