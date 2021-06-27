import React from 'react';
import NavBar from '../components/Navbar';
import { MainContent } from '../styles/MainContent';

export default function DinerLanding ({ token }) {
  return (
    <>
      <NavBar token={token} showSearch={true}/>
      <MainContent>
        <h1>Diner page</h1>
      </MainContent>
    </>
  )
}