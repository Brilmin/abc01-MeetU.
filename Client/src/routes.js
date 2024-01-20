import {
  Route,
  createBrowserRouter,
  createRoutesFromElements,
} from "react-router-dom";

import { lazy } from "react";

const App = lazy(() => import("./App"));
const PageLayout = lazy(() => import("./layout/PageLayout.jsx"));
const PrivateLayout = lazy(() => import("./layout/PrivateLayout.jsx"));
const Introduction = lazy(
  () => import("./pages/Introduction/Introduction.jsx"),
);
const Join = lazy(() => import("./pages/Member/Join.jsx"));
const Login = lazy(() => import("./pages/Member/Login.jsx"));
const Membership = lazy(() => import("./pages/Member/Membership.jsx"));
const Band = lazy(() => import("./pages/Band/Band.jsx"));
const GuestBook = lazy(() => import("./pages/GuestBook/GuestBook.jsx"));
const MyPage = lazy(() => import("./pages/MyPage/MyPage.jsx"));

const router = createBrowserRouter(
  createRoutesFromElements(
    <>
      <Route path="/" element={<App />}>
        <Route element={<PageLayout />}>
          <Route path="introduction" element={<Introduction />} />
          <Route path="join" element={<Join />} />
          <Route path="login" element={<Login />} />
        </Route>
        <Route element={<PrivateLayout />}>
          <Route path="mypage" element={<MyPage />} />
          <Route path="band" element={<Band />} />
          <Route path="guestbook" element={<GuestBook />} />
          <Route path="membership" element={<Membership />} />
        </Route>
      </Route>
    </>,
  ),
);

export default router;
