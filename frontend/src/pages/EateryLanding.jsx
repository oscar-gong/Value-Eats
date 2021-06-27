import React from "react";
import NavBar from "../components/Navbar";
import { MainContent } from "../styles/MainContent";

export default function EateryLanding({ token }) {
  return (
    <>
      <NavBar token={token} showSearch={false}/>
      <MainContent>
          <h1>Eatery page</h1>
      </MainContent>
    </>
  );
}
