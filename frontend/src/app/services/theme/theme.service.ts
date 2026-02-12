import { Injectable } from '@angular/core';

const THEME_KEY = 'student_portal_theme';
type ThemeType = 'dark' | 'light';

@Injectable({
  providedIn: 'root',
})
export class ThemeService {
  private htmlEl: HTMLElement = document.documentElement;
  private currentTheme: ThemeType = 'light';

  constructor() {
    this.initTheme();
  }
  private initTheme(): void {
    const stored = this.loadTheme();
    const theme: ThemeType = stored === 'dark' ? 'dark' : 'light';
    console.log(theme);
    this.applyTheme(theme);
    this.saveTheme(theme);
  }
  private applyTheme(theme: ThemeType): void {
    this.currentTheme = theme;
    this.htmlEl.setAttribute('data-theme', theme);
  }

  private saveTheme(theme: ThemeType): void {
    localStorage.setItem(THEME_KEY, theme);
  }

  private loadTheme(): ThemeType | null {
    return localStorage.getItem(THEME_KEY) as ThemeType | null;
  }

  toggleTheme(): void {
    const next: ThemeType = this.currentTheme === 'dark' ? 'light' : 'dark';
    this.applyTheme(next);
    this.saveTheme(next);
  }

  isDark(): boolean {
    return this.currentTheme === 'dark';
  }

  getTheme(): ThemeType {
    return this.currentTheme;
  }
}
